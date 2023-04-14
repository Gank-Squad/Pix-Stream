import React from 'react';

export default function Invalid(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;


    React.useEffect(() => 
    {
        console.log(`404 Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    return (
        <div>
            {/* display 404 text and button to redirect to home */}
            <center>
                <p class="pt-6 pb-6 text-3xl font-bold">404 unknown page</p>
                <a href={"/home"}><p className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-40">Return Home</p></a>
            </center>
        </div>
    )
}