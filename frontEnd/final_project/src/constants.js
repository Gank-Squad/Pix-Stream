
// max dimensions for media, mainly only effects MediaContainer
export const MAX_DIMENSIONS = {
    preview_width   : 256,
    preview_height  : 256,
    general_width   : 768,
    general_height  : 768,
}


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

// for easier development, so we don't need to change it after every pull
export const SERVER_HOST = ((window.navigator.userAgent.indexOf("Linux") != -1) ? 
"http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/": "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/");

    export const API_ENDPOINTS = {
    media: {
        get_file : `${SERVER_HOST}files/`,
        // should be POST, and send json, files/{filehash}/tag
        add_tag_to_file : `${SERVER_HOST}files/`,
        get_tags : `${SERVER_HOST}tags/`,
        upload_file : `${SERVER_HOST}files/upload/`,
        get_posts : `${SERVER_HOST}posts/`,
    },
    search : {
        get_files_with_tags : `${SERVER_HOST}tags/files/?tags=true`,
    }
};

export const DISPLAY_TYPES = {
    // type for search results, home page
    thumb_preview   : 1,
    // type for media page dispaly
    general_display : 2,
    // likely exclusive use for image, imposes no restriction on width or height
    full_size_display: 3, 
}

export const NO_MEDIA_IMG = "https://media.discordapp.net/attachments/447901281328168962/1094429564563894373/nomedia.png";

// everything above this is a video/not an image
export const MIME_IMG_CUTOFF = 19;

export const API_TEMPLATES = {
    get_file        : { url: `${SERVER_HOST}files/{hash}/`    , method: "GET" },
    get_post        : { url: `${SERVER_HOST}posts/{postId}/`  , method: "GET" },
    get_thumbnail   : { url: `${SERVER_HOST}files/t/{hash}`    , method: "GET" },
    get_ts_file     : { url: `${SERVER_HOST}files/{hash}/{ts_fragment}`, method: "GET" },
    get_file_tags   : { url: `${SERVER_HOST}files/tags`      , method: "GET" },
    add_tag_to_file : { url: `${SERVER_HOST}files/{hash}/tag`, method: "POST" },
    upload_file     : { url: `${SERVER_HOST}files/upload/`   , method: "POST" },
    upload_post     : { url: `${SERVER_HOST}posts/upload/`   , method: "POST" },
    create_tag      : { url: `${SERVER_HOST}tags/create/`   , method: "POST" },
    get_tag_file_count      : { url: `${SERVER_HOST}tags/count/`   , method: "GET" },

    get_files_with_tags    : { url: `${SERVER_HOST}tags/files/` , method: "POST" },
    get_posts_with_tags    : { url: `${SERVER_HOST}tags/posts/` , method: "POST" },
}