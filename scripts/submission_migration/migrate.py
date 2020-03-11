import requests
import json
import csv

SESSION_ID = ''

COOKIES = {
    'JSESSIONID': SESSION_ID,
}
HEADERS = {
    'Cookie': f'JSESSIONID={SESSION_ID};',
    'Content-Type': 'application/json',
}
# BASE_URL = 'http://localhost:8080/openstorefront'
BASE_URL = 'https://spoonsite.com/openstorefront'
# BASE_URL = 'http://spoon-staging.usurf.usu.edu/openstorefront'

USER_SUBMISSIONS = '/api/v1/resource/usersubmissions/admin'

def submit_url(base_url, id):
    return f'{base_url}/api/v1/resource/usersubmissions/{id}/submitforapproval'

def prettyPrint(r):
    print("STATUS: ", r.status_code)
    parsed = json.loads(r.text)
    print(json.dumps(parsed, indent=4))


if __name__ == "__main__":
    res = requests.get(BASE_URL + USER_SUBMISSIONS, cookies=COOKIES, headers=HEADERS, verify=False)
    # prettyPrint(res)

    parsed = json.loads(res.text)

    print(f'Number of partial submissions: {len(parsed)}')

    with open('partial_submissions.csv', 'w') as fout:
        writer = csv.writer(fout, dialect='excel')
        writer.writerow(['Submission Name', 'Owner Username', 'Create User', 'Component Type', 'Submitted', 'Changed to STATUS to NOT SUBMITTED', 'Verified Form', 'Changed Back to Original Owner'])
        for i, val in enumerate(parsed):
            writer.writerow([val['submissionName'], val['ownerUsername'], val['createUser'], val['componentType']])
