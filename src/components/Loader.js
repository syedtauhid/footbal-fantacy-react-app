import React from 'react';

const Loader = () => {
    return (
        <div className="loader" style={{minHeight: '10px'}}>
            Loading Content...
            <div className="loader-small">
                <div className="line"/>
            </div>
        </div>
    )
};

export default Loader;
