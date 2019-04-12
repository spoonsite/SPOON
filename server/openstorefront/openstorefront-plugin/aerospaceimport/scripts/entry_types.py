import requests
import json
import sys
import csv

BASE_URL = 'https://www.spoonsite.com/openstorefront'
ENDPOINT = '/api/v1/resource/componenttypes/nested?_dc=1547489864217&all=true&node=root'

def prettyPrint(r):
    print("URL: ", BASE_URL + ENDPOINT)
    print("STATUS: ", r.status_code)
    parsed = json.loads(r.text)
    print(json.dumps(parsed, indent=4))

def process(child, result):
    # global COUNT
    # global RESULT
    # COUNT += 1
    label = child['componentType']['label']
    componentType = child['componentType']['componentType']
    # print()
    # print(f'Label: {label}')
    # print(f'ComponentType: {componentType}')
    result.append((componentType, label))
    # print()
    children = child['children'] if 'children' in child.keys() else None
    for new_child in children:
        process(new_child, result)
    return result

def get_componentTypes():
    base_url = input(f'Please provide the baseURL to get the component types [{BASE_URL}]: ')
    if base_url == '':
        base_url = BASE_URL
    res = requests.get(base_url + ENDPOINT) 
    # prettyPrint(res)
    parsed = json.loads(res.text)
    children = parsed['children']
    output_str = ''
    result = []
    for child in children:
        result = process(child, result)
    
    return result


if __name__ == "__main__":
    result = get_componentTypes() 
    print(len(result))
    print(result)
    
    # FILENAME = 'output.csv'
    # print(f'COUNT: {COUNT}')
    # print(f'writing csv file to: {FILENAME}')
    # with open(FILENAME, 'w') as fout:
    #     writer = csv.writer(fout)
    #     writer.writerow(['ComponentType', 'Label'])
    #     for item in RESULT:
    #         writer.writerow(item)