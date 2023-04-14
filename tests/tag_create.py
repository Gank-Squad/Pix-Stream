import requests 


url = "http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/tags/create/"

payload = [
    {
        "namespace" : "hello",
        "subtag":"world"
    },
    {
        "namespace" : "test",
        "subtag":"uwu"
    },
    {
        "namespace" : "wife",
        "subtag":"shondo"
    },
]


a = requests.post(url, json=payload)

print(a.status_code)
print(a.reason)