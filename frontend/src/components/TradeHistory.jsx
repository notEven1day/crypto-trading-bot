import { useEffect, useState } from "react";
import "./TradeHistory.css";

export default function TradeHistory() {
    const [trades, setTrades] = useState([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(true);

    const fetchTrades = () => {
        setLoading(true);
        fetch(`http://localhost:8080/bot?page=${page}&size=10`)
            .then(res => res.json())
            .then(data => setTrades(data))
            .catch(err => console.error("Error fetching trades:", err))
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchTrades(); // initial load

        // refresh every 35s
        const interval = setInterval(fetchTrades, 35000);

        return () => clearInterval(interval); // cleanup when unmount
    }, [page]);

    if (loading) return <div className="loading">⏳ Loading trades...</div>;

    return (
        <div className="trade-history">
            <h3>📊 Trade History</h3>

            <table className="trade-table">
                <thead>
                <tr>
                    <th>💰 Currency</th>
                    <th>🏷 Price</th>
                    <th>📦 Quantity</th>
                    <th>⚡ Action</th>
                    <th>📈 Profit</th>
                    <th>📌 Status</th>
                    <th>⏰ Timestamp</th>
                </tr>
                </thead>
                <tbody>
                {trades.length === 0 ? (
                    <tr>
                        <td colSpan="7" className="no-trades">No trades found 🚫</td>
                    </tr>
                ) : (
                    trades.map((trade, idx) => (
                        <tr key={idx}>
                            <td>{trade.currencyName}</td>
                            <td>${trade.price.toFixed(2)}</td>
                            <td>{trade.quantity}</td>
                            <td className={trade.action === "BUY" ? "buy" : "sell"}>
                                {trade.action === "BUY" ? "🟢 BUY" : "🔴 SELL"}
                            </td>
                            <td>
                                {trade.profit !== null ?
                                    (trade.profit >= 0
                                        ? `🟢 $${trade.profit.toFixed(2)}`
                                        : `🔴 $${trade.profit.toFixed(2)}`)
                                    : "-"}
                            </td>
                            <td>{trade.status}</td>
                            <td>{new Date(trade.timestamp).toLocaleString()}</td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>

            <div className="pagination">
                <button
                    onClick={() => setPage(p => Math.max(p - 1, 0))}
                    disabled={page === 0}
                >
                    ⬅ Prev
                </button>
                <span>Page {page + 1}</span>
                <button onClick={() => setPage(p => p + 1)}>
                    Next ➡
                </button>
            </div>
        </div>
    );
}
