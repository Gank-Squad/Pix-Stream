import React from 'react'

export default function HeaderBar({ toggleSidebarVisibility })
{
    function redirect_tags()
    {
        // no point in refreshing a page thats already loaded and wont change
        if (window.location.href.includes("/tags"))
            return;
        window.location.href = '/tags';
    }
    function redirect_upload()
    {
        if (window.location.href.includes("/upload"))
            return;
        window.location.href = '/upload';
    }
    function redirect_home()
    {
        // allow refresh of this in case we ever make backend give random stuff
        window.location.href = '/home';
    }

    return (
        <header className="w-full p-4 h-20 bg-custom-dark-blue text-left ">
                    {/* The following is the code for the hamburger menu
                    but I'm not really sure how to make it show up inline with everything else */}
                    {toggleSidebarVisibility && <button
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l"
                        data-te-sidenav-toggle-ref
                        aria-haspopup="true"
                        onClick={toggleSidebarVisibility}
                        // ref={hamburger}
                        >
                        <span 
                        // className="block [&>svg]:h-5 [&>svg]:w-5 [&>svg]:text-white"
                        >
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 24 24"
                                fill="currentColor"
                                className="h-5 w-5"
                                >
                                    <path
                                    fillRule="evenodd"
                                    d="M3 6.75A.75.75 0 013.75 6h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 6.75zM3 12a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 12zm0 5.25a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75a.75.75 0 01-.75-.75z"
                                    clipRule="evenodd" />
                            </svg>
                        </span>
                    </button>}
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l" onClick={redirect_home}>Home</button>
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 border border-blue-700 px-4" onClick={redirect_tags}>Tags</button>
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-r" onClick={redirect_upload}>Upload</button>
            </header>
    )
}