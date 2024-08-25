from bs4 import BeautifulSoup as soup
from decimal import *

# Anti python decimal jank mechanism
getcontext().prec = 4

# Skill lists
sword_skills = ['sever artery', 'gash', 'hamstring', 'savage leap', 'final thrust', 'impale', 'riposte', 'flaming flurry']
torch_skills = ['blaze breaker', 'flames of war']
longbow_skills = ['dual shot', 'fan of fire', 'arcing arrow', 'smoldering arrow', 'pin down', 'scorched earth']
unarmed_skills = ['blood reckoning', 'shattering blow', 'sundering leap', 'head butt']

# Open and read log html
HTMLFile = open("Standard Kitty Golem.html")
index = HTMLFile.read()
page_soup = soup(index, 'html.parser')
rotation_spans = page_soup.findAll('span', {'class': 'rot-skill'})

# Keep track of time spent so we can add gaps.
currentTime = Decimal(0.0)

# Loop through every rotation step and print formatted line
for span in rotation_spans:
    # Fetch image tooltip text and do initial formatting to retrieve skill name
    step_text = span.img['data-original-title']
    step_temp = step_text.split(" at ")
    step_skill = step_temp[0]

    # We don't care about sigils or king of fires procs.
    if step_skill.lower().strip() in ['superior sigil of doom', 'ring of earth (superior sigil of geomancy)', 'king of fires']:
        continue

    # Outrage is special so it gets special treatment
    if step_skill.lower().strip() == 'outrage':
        print('unarmed.outrage()')
        continue

    # Weapon Swap is special so it gets special treatment
    if step_skill.lower().strip() == 'weapon swap':
        print('unarmed.swapWeapon(0.0)')
        continue

    # Fetch cast time and currentTime tracker for rotation step
    cast_currentTime = Decimal(step_temp[1].split("for")[0].split("s")[0].strip())
    cast_time = Decimal(int(step_temp[1].split("for")[1].split("ms")[0].strip()) / Decimal(1000))

    # Emulator can't handle negative start times so we only use the excess cast time after 0s
    if cast_currentTime < 0:
        cast_time = max(cast_currentTime + cast_time, Decimal(0))
    # If time tracker doesn't match the skill tracker then add a delay to resynchronize.
    elif currentTime < cast_currentTime:
        diff = cast_currentTime - currentTime
        currentTime += diff
        print('unarmed.wait(' + str(diff) + ')')

    #Update time tracker
    currentTime += cast_time

    # Cancelled skills are dead skills
    if 'rot-cancelled' in span.img.attrs['class']:
        print('unarmed.wait(' + str(cast_time) + ')')
        continue

    #Print formatted rotation step depending on weapon
    partToPrint = step_skill.lower().strip().replace(' ', '_') + '(' + str(cast_time) + ')'
    if step_skill.lower().strip() in sword_skills:
        print('sword.' + partToPrint)
    elif step_skill.lower().strip() in torch_skills:
        print('torch.' + partToPrint)
    elif step_skill.lower().strip() in longbow_skills:
        print('longbow.' + partToPrint)
    elif step_skill.lower().strip() in unarmed_skills:
        print('unarmed.' + partToPrint)
