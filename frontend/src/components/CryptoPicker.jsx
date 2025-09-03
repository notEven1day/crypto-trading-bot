import { useEffect, useState } from "react";
import "./CryptoPicker.css";

export default function CryptoPicker( ) {
    const [cryptos, setCryptos] = useState([]);
    const [searchName, setSearchName] = useState("");
    const [searchSymbol, setSearchSymbol] = useState("");
    const [filtered, setFiltered] = useState([]);
    const [isOpen, setIsOpen] = useState(false);

    // Fetch all cryptos on mount
    useEffect(() => {
        fetch("http://localhost:8080/currencies/all")
            .then(res => res.json())
            .then(data => setCryptos(data))
            .catch(err => console.error(err));
    }, []);

    const handleSearch = () => {
        let results = cryptos;

        if (searchName) {
            const lowerName = searchName.toLowerCase();
            results = results.filter(c => c.name.toLowerCase() === lowerName); // exact match
        }

        if (searchSymbol) {
            const lowerSymbol = searchSymbol.toLowerCase();
            results = results.filter(c => c.symbol.toLowerCase() === lowerSymbol); // exact match
        }

        // If both empty, default to first 5
        if (!searchName && !searchSymbol) {
            results = cryptos.slice(0, 5);
        }

        setFiltered(results);
        setIsOpen(true);
    };

    const selectCrypto = crypto => {
        fetch(`http://localhost:8080/select-crypto`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: crypto.name,
                coingeckoId: crypto.coingecko_id
            })
        }).catch(err => console.error(err));
        setIsOpen(false);
    };


    return (
        <div className="crypto-picker">
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Search by Name"
                    value={searchName}
                    onChange={e => setSearchName(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="Search by Symbol"
                    value={searchSymbol}
                    onChange={e => setSearchSymbol(e.target.value)}
                />
                <button onClick={handleSearch}>Search</button>
            </div>
            {isOpen && (
                <ul className="dropdown">
                    {filtered.length > 0 ? (
                        filtered.map(c => (
                            <li
                                key={c.coingecko_id}
                                className="crypto-item"
                                onClick={() => selectCrypto(c)}
                            >
                                {c.name} ({c.symbol.toUpperCase()})
                            </li>
                        ))
                    ) : (
                        <li className="crypto-item">No results</li>
                    )}
                </ul>
            )}
        </div>
    );
}
