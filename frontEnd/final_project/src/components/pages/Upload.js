import React from 'react';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;


    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);



    function redirect_tags()
    {
        window.location.href = '/tags';
    }
    function redirect_upload()
    {
        window.location.href = '/upload';
    }
    function redirect_home()
    {
        window.location.href = '/home';
    }

    return (
        <div class="flex flex-col h-screen overflow-hidden">
            <header class="w-full p-4 h-20 bg-custom-dark-blue">
                <table>
                <td><div class="text-left inline-block">
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l" onClick={redirect_home}>Home</button>
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 border border-blue-700 px-4" disabled onClick={redirect_tags}>Tags</button>
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-r" onClick={redirect_upload}>Upload</button>
                </div></td>

                <td class="center"><div class="text-center inline"><p class="text-xl font-bold">PixStream</p></div></td>
                </table>
            </header>
        </div>
    )
}