import React from 'react';
import './App.css';
import RegistrationLayout from "./components/RegistrationLayout";
import ProfileHeading from "./components/ProfileHeading";

function App() {
    return (
        <div className="App">
            <header className="App-header">
                <ProfileHeading/>
                <RegistrationLayout/>
            </header>
        </div>
    );
}

export default App;
