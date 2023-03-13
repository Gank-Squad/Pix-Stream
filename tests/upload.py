import requests

def upload():

    file = "C:\\Users\\alice\\Pictures\\screenshots\\00000000000000000006.jpg"

    payload = {
        "data": open(file, "rb")
    }

    a = requests.post("http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/media/upload/", files=payload)

    print(a.status_code)
    print(a.text)


upload()