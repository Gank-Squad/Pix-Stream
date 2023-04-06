import requests

DATA = "./data_files/"

DATA = "E:/Data/"

def upload():

    # file = DATA + "/img/2.jpg"
    # file = DATA + "/bad/conjure.exe"
    # file = DATA + "/vid/1.mp4"
    # file = DATA + "/vid/2.mkv"
    # file = DATA + "/img/4.jpg"
    # file = DATA + "4.mp4"
    file = DATA + "6.png"
    # file = DATA + "3.mp4"
    # file = DATA + "2.jpg"


    payload = {
        "data": open(file, "rb")
    }

    a = requests.post("http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/upload/", files=payload)

    print(a.status_code)
    print(a)

def post_tags():

    payload = [{
        "tag_id": 12
    }]
    a = requests.post("http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/tags/files/?tags=true&limit=0",
    json=payload)

    print(a)
    print(a.status_code)

    if(a.status_code == 200):
        print(a.json())

# post_tags()
upload()