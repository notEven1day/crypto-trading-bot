import Header from "./components/Header";
import Content from "./components/Content";
import Footer from "./components/Footer";
import './App.css';

export default function App() {
    return (
        <div className="app-container">
            <Header />
            <Content />
            <Footer />
        </div>
    );
}
