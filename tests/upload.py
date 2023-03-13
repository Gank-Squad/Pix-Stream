import requests

def upload():

    file = "C:\\1.png"

    payload = {
        "data": open(file, "rb")
    }

    a = requests.post("http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/media/upload/", files=payload)

    print(a.status_code)
    print(a.text)


upload()