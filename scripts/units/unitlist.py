import requests
import json
import sys

"""
This script hits the endpoint checking that a list of units
have the same dimension and are compatible
"""

# get your sessionID and token from the browser after you login
# to the application
sessionID = 'YOUR-SESSION-ID'
token = 'YOUR-TOKEN'

cookies = {
    'JSESSIONID': sessionID,
    'visited': 'yes',
    'X-Csrf-Token': token
}
headers = {
    'Cookie': f'JSESSIONID={sessionID}; X-Csrf-Token={token}; visited=yes',
    'Content-Type': 'application/json',
    'X-Csrf-Token': token,
}
baseURL = 'http://localhost:8080/openstorefront'
endpoint = '/api/v1/resource/attributes/unitlistcheck'


def prettyPrint(r):
    print("URL: ", baseURL + endpoint)
    print("STATUS: ", r.status_code)
    parsed = json.loads(r.text)
    print(json.dumps(parsed, indent=4))


if __name__ == "__main__":
    data = {
        'baseUnit': 'mm',
        'units': ['cm', 'm', 'mi', 'in']
    }
    r = requests.post(baseURL + endpoint, data=json.dumps(data), cookies=cookies, headers=headers)
    print(r)
    print(r.text)
