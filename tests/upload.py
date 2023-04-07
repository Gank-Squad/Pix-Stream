import requests

DATA = "./data_files/"

# DATA = "E:/Data/"

host = "192.168.1.148"
def upload():

    # file = DATA + "/img/2.jpg"
    # file = DATA + "/bad/conjure.exe"
    # file = DATA + "/vid/1.mp4"
    # file = DATA + "/vid/2.mkv"
    # file = DATA + "/img/4.jpg"
    # file = DATA + "4.mp4"
    # file = DATA + "6.png"
    file = DATA +  "/vid/1.mp4"
    # file = DATA + "2.jpg"
    # file = DATA + "vid.flac"


    payload = {
        "data": open(file, "rb")
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/files/upload/", files=payload)

    print(a.status_code)
    print(a.json())

def uploadpost():

    file = DATA + "/img/2.jpg"
    # file = DATA + "/bad/conjure.exe"
    # file = DATA + "/vid/1.mp4"
    # file = DATA + "/vid/2.mkv"
    # file = DATA + "/img/4.jpg"
    # file = DATA + "4.mp4"
    # file = DATA + "6.png"
    # file = DATA +  "/vid/1.mp4"
    # file = DATA + "2.jpg"
    # file = DATA + "vid.flac"


    payload = {
        "data": open(file, "rb"),
        "title" : "Hello world, this is a title",
        "description": "nyh nyahn yhan hyna la "
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
    else:
        print(a.status_code)
        print(a.reason)

def post_tags():

    payload = [{
        "tag_id": 12
    }]
    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/tags/files/?tags=true&limit=0",
    json=payload)

    print(a)
    print(a.status_code)

    if(a.status_code == 200):
        print(a.json())

def add_tags(tag_ids : list[int], file_hash: str):
    
    payload = [{ "tag_id" : t } for t in tag_ids]

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/files/{file_hash}/tag", json=payload)
    print(a)
    print(a.status_code)


# post_tags()
uploadpost()
# add_tags([1, 2, 3, 4, 5, 6], "1073d143a1848e66c9a093eeca19ba6c76608c150a056b7208fc8c369ee1a386")
