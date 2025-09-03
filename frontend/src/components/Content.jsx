import './Content.css';
import CryptoPicker from "./CryptoPicker.jsx";
import TradingButton from "./TradingButton.jsx";
import LoadFundsButton from "./LoadFundsButton.jsx";
import ModePicker from "./ModePicker.jsx";
import WalletBalance from "./WalletBalance.jsx";
import TradeHistory from "./TradeHistory.jsx";
import UpdateCryptoList from "./UpdateCryptoList.jsx";

export default function Content() {
    return (
        <main className="content">
            <UpdateCryptoList/>
            <CryptoPicker />

            <div className="button-group">
                <ModePicker/>
                <LoadFundsButton />
                <TradingButton />
            </div>

            <div className="welcome-text">
                🚀 Welcome to the Trading Bot Dashboard
            </div>

            <WalletBalance/>
            <TradeHistory/>
        </main>
    );
}
