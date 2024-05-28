import './App.css';
import logo from '../logo.svg';
import Header from './Header';
import AppContent from './AppContent';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'; // Import BrowserRouter as Router

import About from './contents/About';

function App() {
    return (
        <Router> {/* Wrap your routes with Router */}
            <div>
                <Header pageTitle={"EventSpot"} logoSrc={logo} />
                <div className={"container-fluid"}>
                    <div className={"row"}>
                        <div className={"col"}>
                            <Routes>
                                <Route path="/login" element={<AppContent />} />
                                <Route path="/about" element={<About />} />
                            </Routes>
                        </div>
                    </div>
                </div>
            </div>
        </Router>
    );
}

export default App;
