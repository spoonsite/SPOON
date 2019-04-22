import requests
import json
import sys

SESSION_ID = 'BB7277E47A5965425BD6574C1CB65B1C'
TOKEN = ''

COOKIES = {
    'JSESSIONID': SESSION_ID,
}
HEADERS = {
    'Cookie': f'JSESSIONID={SESSION_ID};',
    'Content-Type': 'application/json',
}
BASE_URL = 'http://localhost:8080/openstorefront'
ENDPOINT = '/api/v1/resource/attributes/attributetypes'

SKIPPED_FILENAME = 'skipped.txt'
UNIT_LIST_MAP = {}
ATTRIBUTE_MAP = {}

########################## Hit Ryan's endpoint to see if valid unit ##########################
def checkUnit(unit, dimension=False):
    data = {
        'unit': unit
    }
    res = requests.post(BASE_URL + '/api/v1/resource/attributes/unitcheck', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
    try:
        parsed = json.loads(res.text)
        if dimension:
            print(f"Dimension:        {parsed['dimension']}")
        else:
            print('<---------- Unit check ---------->')
            print(f"Unit:          {parsed['unit']}")
            print(f"Dimension:     {parsed['dimension']}")
            print(f"SI Unit:       {parsed['standardUnit']}")
    except:
        print('Something went wrong')
        print(res.text)

##########################Hit Ryan's endpoint to see if valid unit list##########################
def checkUnitList(unit, unit_list):
    data = {
        'baseUnit': unit,
        'units': unit_list
    }
    res = requests.post(BASE_URL + '/api/v1/resource/attributes/unitlistcheck', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
    try:
        parsed = json.loads(res.text)
        print('<---------- Unit List Check ---------->')
        print(f"Dimension:        {parsed['dimension']}")
        print(f"SI Unit:       {parsed['standardUnit']}")
    except:
        print('Something went wrong')
        print(res.text)

######################### find duplicates #########################
def computeDuplicates(attributes):
    seen = {}
    attr = {} 
    dupes = []

    for attribute in attributes:
        des = attribute['description']
        if des not in seen:
            seen[des] = 1
        else:
            if seen[des] == 1:
                dupes.append(des)
            seen[des] += 1
    return dupes