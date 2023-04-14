
import requests
import os 

DATA = "/home/minno/Pictures/UploadShowcase/"
host = "192.168.1.148"

def tag_file(file_hashs, tag_id):
    url = f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/files/{file_hashs}/tag"
    print(url)
    payload = [
        {
            "tag_id" : tag_id,
        },
    ]

    a = requests.post(url, json=payload)

    if a.status_code != 200:
        print(a.reason)

    print(a.status_code)


def create_tag(namespace, subtag):
    url = f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/tags/create/"

    payload = [
        {
            "namespace" : namespace,
            "subtag":subtag
        },
    ]

    a = requests.post(url, json=payload)

    if a.status_code != 200:
        print(a.reason)
    return a.json()

def uploadpost():

    file = os.path.join(DATA, "5e048baca86ebcfc09c4e94a7f3d704779cfaaca60a86e7a02a59909048eb2d2.jpg")
    payload = {
        "data": open(file, "rb"),
        "title" : "Soldier Drawing I made!",
        "description": """Was doing some random sketching and decided to post this drawing I made! <3 """
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        creator = create_tag("artist", "ilikeDogs24")[0]["tag_id"]
        category = create_tag("category", "digital art")[0]["tag_id"]
        eeeee = create_tag("", "sketch")[0]["tag_id"]
        eeee = create_tag("", "lineart")[0]["tag_id"]
        eee = create_tag("", "gun")[0]["tag_id"]
        ee = create_tag("", "multiple views")[0]["tag_id"]
        e = create_tag("", "1person")[0]["tag_id"]

        tag_file(a['files'][0]['hash'], creator)
        tag_file(a['files'][0]['hash'], category)
        tag_file(a['files'][0]['hash'], e)
        tag_file(a['files'][0]['hash'], ee)
        tag_file(a['files'][0]['hash'], eee)
        tag_file(a['files'][0]['hash'], eeee)
        tag_file(a['files'][0]['hash'], eeeee)
        e= create_tag("", "safe")[0]["tag_id"]
        tag_file(a['files'][0]['hash'], e)
    else:
        print(a.status_code)
        print(a.reason)







    file = os.path.join(DATA, "96e2fd2d27515d38722600b452f8ff87d0051b77f558d2f519521e6591252b01.webp")
    payload = {
        "data": open(file, "rb"),
        "title" : "My New Wallpaper!",
        "description": """Saw this wallpaper someone posted somewhere and I Love it !!!, thought I would share"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
            create_tag("artist", "kimchi2")[0]["tag_id"],
        create_tag("category", "digital art")[0]["tag_id"]
        ,create_tag("", "wallpaper")[0]["tag_id"]
        ,create_tag("", "wallpaper horizontal")[0]["tag_id"]
        , create_tag("", "high resolution")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("", "1girl")[0]["tag_id"]
        ,create_tag("", "1person")[0]["tag_id"]
        ,create_tag("", "dark color")[0]["tag_id"]
        ,create_tag("", "1080p")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)








    file = os.path.join(DATA, "367bf277501a7a042d3d05962c8072b1470f8d40f26331ea500ae0ee99047d41.jpg")
    payload = {
        "data": open(file, "rb"),
        "title" : "Meme i made for Chainsaw Man",
        "description": """Pochita is truly the best"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "memes")[0]["tag_id"]
        ,create_tag("character", "pochita")[0]["tag_id"]
        ,create_tag("series", "chainsaw man")[0]["tag_id"]
        , create_tag("", "capybara")[0]["tag_id"]
        ,create_tag("character", "wojak")[0]["tag_id"]
        ,create_tag("character", "chad")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("", "multiple views")[0]["tag_id"]
        ,create_tag("", "multiple people")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)














    file = os.path.join(DATA, "540c4de6d3f9853426d6b82ba38567d4aa597fdb054c68a3f5becf7c05c1883e.gif")
    payload = {
        "data": open(file, "rb"),
        "title" : "Watamelon",
        "description": """melon of wata"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "memes")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("character", "watamelon")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)












    file = os.path.join(DATA, "4183d01344f0bcb9eb56e0cfb1156a4d261c09f7066983009107fc3fcd1152e8.png")
    payload = {
        "data": open(file, "rb"),
        "title" : "Fellow Brrats... we lost",
        "description": """hahaha jokes on you this is a sick prank! nothing bad happened"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "memes")[0]["tag_id"]
        ,create_tag("character", "brat")[0]["tag_id"]
        ,create_tag("", "rat")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("", "transparent")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)










    file = os.path.join(DATA, "64173aa92064ed31e1fc3175dfd6549f3552f6949d69bc071bd0b229b72d09ad.webp")
    payload = {
        "data": open(file, "rb"),
        "title" : "My latest art!!",
        "description": """This took me over 40 hours!!
        I'm glad to be done I love how it turned out,
        I hope you guys like it !"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "digital art")[0]["tag_id"],
            create_tag("artist", "Malhar")[0]["tag_id"],
        create_tag("category", "digital art")[0]["tag_id"]
        ,create_tag("", "wallpaper")[0]["tag_id"]
        ,create_tag("", "wallpaper horizontal")[0]["tag_id"]
        , create_tag("", "high resolution")[0]["tag_id"]
        ,create_tag("", "1girl")[0]["tag_id"]
        ,create_tag("", "1person")[0]["tag_id"]
        ,create_tag("", "dark color")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("", "1080p")[0]["tag_id"]
        ,create_tag("", "deep meaning")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)












    file = os.path.join(DATA, "ae8c87ef0a9bbbeef89ec4e90b00159a699e11c83fde67d7ad86cc38dbacc284.webp")
    payload = {
        "data": open(file, "rb"),
        "title" : "Apple Worm Level 7",
        "description": """THIS is the best level in Apple Worm,
        You cannot tell me otherwise, this is perfection."""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "games")[0]["tag_id"],
        create_tag("character", "Apple Worm")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("series", "Apple Worm")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)








    file = os.path.join(DATA, "b12a6eb996758ec46ab74eb13d09c0e4541cda06b6c8b80628dc5b791814aabf.png")
    payload = {
        "data": open(file, "rb"),
        "title" : "Doko",
        "description": """huh???!?, you think I won't explode??, just watch me"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "memes")[0]["tag_id"],
        create_tag("character", "Doko")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("", "transparent")[0]["tag_id"]
        ,create_tag("", "cute")[0]["tag_id"]
        ,create_tag("", "chibi")[0]["tag_id"]
        ,create_tag("", "open mouth")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)










    file = os.path.join(DATA, "b048ed50d628c1fe4729784964bd60234cf3e3b830825715a427474b0887e77f.png")
    payload = {
        "data": open(file, "rb"),
        "title" : "REJECT MODERNITY",
        "description": """RETURN TO WATAMELON"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "memes")[0]["tag_id"]
        ,create_tag("character", "watamelon")[0]["tag_id"]
        ,create_tag("", "caption")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("", "text")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)





    file = os.path.join(DATA, "c44b1d0dbaf5bb65ab67ee2283a3d331bb0f1d69efe22208dc134c23445337e7.jpg")
    payload = {
        "data": open(file, "rb"),
        "title" : "Why don't you have abs?",
        "description": """What's stopping you from lookin like this?"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "memes")[0]["tag_id"]
        ,create_tag("character", "Apple Worm")[0]["tag_id"]
        ,create_tag("series", "Apple Worm")[0]["tag_id"]
        ,create_tag("character", "abs")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("artist", "someguy44")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)






    file = os.path.join(DATA, "c064c85debe909098ce5bee28e0514c90e4308853012ca3be889006ef02dcb8b.png")
    payload = {
        "data": open(file, "rb"),
        "title" : "Oh my... Headache",
        "description": """his head hur"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "memes")[0]["tag_id"]
        ,create_tag("", "fire")[0]["tag_id"]
        ,create_tag("", "explosion")[0]["tag_id"]
        ,create_tag("", "text")[0]["tag_id"]
        ,create_tag("", "twitter")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("", "cat")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)








    file = os.path.join(DATA, "da19235680973865ab1b91049c3ab7cca20c7d97d23c494e519df98a56ab3ef7.jpg")
    payload = {
        "data": open(file, "rb"),
        "title" : "BY HIS MARK",
        "description": """YOU SHALL KNOW THE FALSE SHEPHERD BY HIS MARK!"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "memes")[0]["tag_id"]
        ,create_tag("character", "watamelon")[0]["tag_id"]
        ,create_tag("", "text")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)








    file = os.path.join(DATA, "f08024bc16eacf6322bf2ff6a8b9bee5c550208e21f810995575965c027621b1.webp")
    payload = {
        "data": open(file, "rb"),
        "title" : "Apple Worm Fanart",
        "description": """Decided to draw Apple Worm with one of my butterfly nifies ;3c"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()
        t = [
        create_tag("category", "digital art")[0]["tag_id"]
        ,create_tag("character", "Apple Worm")[0]["tag_id"]
        ,create_tag("series", "Apple Worm")[0]["tag_id"]
        ,create_tag("", "transparent")[0]["tag_id"]
        ,create_tag("", "weapon")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ,create_tag("", "butterfly knife")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)

    else:
        print(a.status_code)
        print(a.reason)




    file = os.path.join(DATA, "revenge.mp4")
    payload = {
        "data": open(file, "rb"),
        "title" : "'Revenge' - A Minecraft Parody of Usher's DJ Got Us Fallin' In Love (Music Videos)",
        "description": """Creeper Aw Man, back with original audio.
Songs playlist ►    • "Fallen Kingdom" ...  
Creeper aw man merch: http://represent.com/captainsparklez

The people involved:
TryHardNinja - Lead vocals:
   / tryhardninja  
Doc Exx Music - Audio production
CaptainSparklez - animation, set design, post production, lyrics, rap vocals:
   / captainsparklez  
Bootstrap Buckaroo - modeling, rigging, and being the most helpful person ever:
   / bootstrapbuckaroo  

Original Song By Usher:    • Usher - DJ Got Us..."""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()

        t = [
         create_tag("", "music videos")[0]["tag_id"],
         create_tag("category", "music")[0]["tag_id"]
         ,create_tag("category", "games")[0]["tag_id"]
         ,create_tag("series", "minecraft")[0]["tag_id"]
        , create_tag("parody", "minecraft")[0]["tag_id"]
         ,create_tag("song", "DJ Got Us Fallin' In Love")[0]["tag_id"]
          ,  create_tag("artist", "CaptainSparklez")[0]["tag_id"]
          ,  create_tag("artist", "Usher")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
          ,  create_tag("artist", "TryHardNinja")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)
    else:
        print(a.status_code)
        print(a.reason)







    file = os.path.join(DATA, "tnt.mp4")
    payload = {
        "data": open(file, "rb"),
        "title" : "'TNT' - A Minecraft Parody of Taio Cruz's Dynamite (Music Videos)",
        "description": """My first Minecraft music videos, back with its original audio.
Watch Revenge:    • Video  
Songs playlist ►    • "TNT" - A Minecra...  

The people involved:
TryHardNinja - Lead vocals:
   / tryhardninja  

Doc Exx Music - Audio engineering

CaptainSparklez (Myself) - Visuals, instrumental, lyrics: 
   / captainsparklez  

Original "Dynamite" song by Taio Cruz:    • Taio Cruz - Dynam...  

This video + song was exactly one month in the making (Both myself and Igor have other full time obligations), and required a substantial amount of time and effort to complete. If you enjoyed it, share it with someone else you think may enjoy it, too! Ratings and favorites are also extremely helpful in spreading the video around :) Subscribe if you're interested in more!
"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()

        t = [
         create_tag("", "music videos")[0]["tag_id"],
         create_tag("category", "music")[0]["tag_id"]
         ,create_tag("category", "games")[0]["tag_id"]
         ,create_tag("series", "minecraft")[0]["tag_id"]
        , create_tag("parody", "minecraft")[0]["tag_id"]
         ,create_tag("song", "Dynamite")[0]["tag_id"]
          ,  create_tag("artist", "CaptainSparklez")[0]["tag_id"]
          ,  create_tag("artist", "TryHardNinja")[0]["tag_id"]
          ,  create_tag("artist", "Doc Exx Music")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
          ,  create_tag("artist", "Taio Cruz")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)
    else:
        print(a.status_code)
        print(a.reason)





    file = os.path.join(DATA, "bacod.mp4")
    payload = {
        "data": open(file, "rb"),
        "title" : "Sana.... IT'S BACOD!!",
        "description": """kobo trying to get sana to say baccod, and she just cannot
        decided to animate this hope you like it !"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()

        t = [
         create_tag("category", "animation")[0]["tag_id"]
         ,create_tag("series", "hololive")[0]["tag_id"]
        , create_tag("character", "tsukino sana")[0]["tag_id"]
         ,create_tag("character", "kobo kaneru")[0]["tag_id"]
          ,  create_tag("artist", "nami-chan")[0]["tag_id"]
          ,  create_tag("", "text")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
          ,  create_tag("", "blue hair")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)
    else:
        print(a.status_code)
        print(a.reason)




    file = os.path.join(DATA, "zg.mkv")
    payload = {
        "data": open(file, "rb"),
        "title" : "Apple Worm Zero Gravity",
        "description": """I figured out how to disable gravity in Apple Worm, so here is a video about that"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()

        t = [
         create_tag("category", "games")[0]["tag_id"]
         ,create_tag("series", "Apple Worm")[0]["tag_id"]
        , create_tag("character", "Apple Worm")[0]["tag_id"]
          ,  create_tag("artist", "nami-chan")[0]["tag_id"]
          ,  create_tag("", "text")[0]["tag_id"]
          ,  create_tag("", "gameplay")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)
    else:
        print(a.status_code)
        print(a.reason)





    file = os.path.join(DATA, "badapple.mp4")
    payload = {
        "data": open(file, "rb"),
        "title" : "Bad Apple!!",
        "description": """I did a montage of the video "Bad Apple!!" to go with the full version. The song is sang by Nomico and made by Alstroemeria Records.

At some places, it's not lag. It's just the video that is slow down.

==How to see the subtitles (updated on July 3rd, 2020)==
Those who are not familiar with the YouTube Captions, you just have to press the settings button and go to "Subtitles/CC", it will give you those options:
- "English - Translation in English" - Turn on the English Captions
- "Japanese - Romaji" - Turn on the Romaji Captions
-  Off
I hope that was helpful. :)

==Various Description Edits==
Edit 28-12-2012: 1,000,000 views to end the year! I never thought that the video will actually have a million views. Thanks everyone! :)

Edit 13-11-2013: Wow, 2,000,000 views! That's so incredible!!!  :D

Edit 27-03-2018: Anyone mentioning "Undertale" in the comments gets filtered.

Edit 03-07-2020: 27 million views. Wow. I still can't believe an edit of the original Bad Apple!! video that I made when I was 15 years old got this many views."""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()

        t = [
         create_tag("category", "music")[0]["tag_id"],
         create_tag("category", "animation")[0]["tag_id"]
         ,create_tag("category", "music videos")[0]["tag_id"]
         ,create_tag("series", "touhou")[0]["tag_id"]
        , create_tag("character", "hakurei reimu")[0]["tag_id"]
          ,  create_tag("", "monochrome")[0]["tag_id"]
          ,  create_tag("", "safe")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)
    else:
        print(a.status_code)
        print(a.reason)








    file = os.path.join(DATA, "dark.flac")
    payload = {
        "data": open(file, "rb"),
        "title" : "Dark Angels - Right Now (Original Version)",
        "description": """Found this eurobeat song online, have a listen"""
    }

    a = requests.post(f"http://{host}:8080/FinalProject-1.0-SNAPSHOT/api/posts/upload/", files=payload)

    if(a.status_code == 200):
        print(a.status_code)
        print(a.json())
        a = a.json()

        t = [
         create_tag("category", "music")[0]["tag_id"],
         create_tag("category", "eurobeat")[0]["tag_id"]
          ,  create_tag("artist", "Dark Angels")[0]["tag_id"]
        ]
        for i in t:
            tag_file(a['files'][0]['hash'], i)
    else:
        print(a.status_code)
        print(a.reason)


uploadpost()