
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


// export const SERVER_HOST = "http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/"
export const SERVER_HOST = "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/"

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
