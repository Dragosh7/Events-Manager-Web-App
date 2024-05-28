import React from 'react';
import './Texts.css';
export default class About extends React.Component {
    render() {
        return (
            <div className="flex justify-center items-center h-screen text-black">
                <div className="container mx-auto text-center">
                    <h1 className="title">About EventSpot</h1>
                    <p className="text-lg mt-4 animate-fade-in">
                        EventSpot is your one-stop destination to discover, purchase tickets, and make reservations
                        for a wide range of events including concerts, theatres, festivals, and more.
                    </p>
                    <p className="text-lg mt-4 animate-fade-in">
                        As an event manager, you can also list and sell tickets to events you organize.
                    </p>
                    <div className="mt-8 animate-slide-up">
                        <h2 className="text-2xl font-bold mb-2">Contact Us</h2>
                        <p className="text-lg mb-4">Have questions or feedback? We'd love to hear from you!</p>
                        <div className="flex justify-center">
                            <a href="mailto:contact@eventspot.com" className="text-lg mr-4 hover:underline">Email Us</a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
