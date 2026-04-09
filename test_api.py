import json
import urllib.request
import urllib.error

url = 'https://csp40zdq-8000.inc1.devtunnels.ms/api/auth/assessments/'
payload = {
    'patient_id': 'P01',
    'patient_name': 'Unknown Patient',
    'patient_dob': '01/01/2000',
    'patient_age': 30,
    'embryo_count': 1,
    'embryo_day': 'Day 5',
    'culture_duration': '72 hours',
    'questions_data': {
        'culture_medium': 'G-100',
        'media_color_change': 'Mild',
        'ph_deviation': 'Normal',
        'visual_clarity': 'Clear',
        'notes': 'None'
    }
}
data = json.dumps(payload).encode('utf-8')
headers = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer mock_access_token_for_Haresh',
    'X-Tunnel-Skip-Anti-Phishing-Page': 'true'
}

req = urllib.request.Request(url, data=data, headers=headers)
try:
    with urllib.request.urlopen(req) as response:
        print(f"Status: {response.getcode()}")
        print(response.read().decode('utf-8'))
except urllib.error.HTTPError as e:
    print(f"Error: {e.code}")
    print(e.read().decode('utf-8'))
except urllib.error.URLError as e:
    print(f"URL Error: {e.reason}")
