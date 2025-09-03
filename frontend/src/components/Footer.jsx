import './Footer.css';

export default function Footer() {
    return (
        <footer className="footer">
            © {new Date().getFullYear()} TRADING BOT. All rights reserved.
        </footer>
    );
}
