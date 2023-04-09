
export const ROUTES = {
    default  : "/",
    home     : "/home",
    // Do not want "watch", "images" and "image" to be 
    // separate with current plans, where all *can* be accessible at once
    // watch    : "/watch",
    // images   : "/images",
    // image    : "/image",
    login    : "/login",
    register : "/register",
    upload   : "/upload",
    media    : "/media",
    search_results : "/results",
    tags     : "/tags",
    upload   : "/upload",
    user     : "/user",
}

// This seems to work but also causes errors in the console so, more testing is needed
export const SERVER_HOST = ((window.navigator.userAgent.indexOf("Linux") != -1) ? 
"http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/": "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/");

    export const API_ENDPOINTS = {
    media: {
        get_file : `${SERVER_HOST}files/`,
        // should be POST, and send json, files/{filehash}/tag
        add_tag_to_file : `${SERVER_HOST}files/`,
        get_tags : `${SERVER_HOST}tags/`,
        upload_file : `${SERVER_HOST}files/upload/`
    },
    search : {
        get_files_with_tags : `${SERVER_HOST}tags/files/?tags=true`,
    }
};


export const API_TEMPLATES = {
    get_file        : { url: `${SERVER_HOST}files/{hash}/`    , method: "GET" },
    get_post        : { url: `${SERVER_HOST}posts/{postId}/`  , method: "GET" },
    get_thumbnail   : { url: `${SERVER_HOST}files/t/{hash}`    , method: "GET" },
    get_ts_file     : { url: `${SERVER_HOST}files/{hash}/{ts_fragment}`, method: "GET" },
    get_file_tags   : { url: `${SERVER_HOST}files/tags`      , method: "GET" },
    add_tag_to_file : { url: `${SERVER_HOST}files/{hash}/tag`, method: "POST" },
    upload_file     : { url: `${SERVER_HOST}files/upload/`   , method: "POST" },
    upload_post     : { url: `${SERVER_HOST}posts/upload/`   , method: "POST" },

    get_files_with_tags    : { url: `${SERVER_HOST}tags/files/` , method: "POST" },
    get_posts_with_tags    : { url: `${SERVER_HOST}tags/posts/` , method: "POST" },
}