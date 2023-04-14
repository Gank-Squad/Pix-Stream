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

    return (
        <div>
            <p>
                User profile page, no planned functionality
            </p>
            <a href={"/home"}><p className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-40">Return Home</p></a>
        </div>
    )
}