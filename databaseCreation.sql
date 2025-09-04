--
-- PostgreSQL database dump
--


-- Dumped from database version 16.10
-- Dumped by pg_dump version 16.10

-- Started on 2025-09-04 16:33:39

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 16757)
-- Name: currency; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.currency (
    id bigint NOT NULL,
    symbol character varying(50) NOT NULL,
    coingecko_id character varying(50) NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.currency OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16756)
-- Name: currency_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.currency_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.currency_id_seq OWNER TO postgres;

--
-- TOC entry 4827 (class 0 OID 0)
-- Dependencies: 215
-- Name: currency_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.currency_id_seq OWNED BY public.currency.id;


--
-- TOC entry 220 (class 1259 OID 16774)
-- Name: holding_stock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.holding_stock (
    id bigint NOT NULL,
    wallet_id bigint NOT NULL,
    currency_id bigint NOT NULL,
    average_price numeric(18,8) NOT NULL,
    quantity numeric(18,8) DEFAULT 0 NOT NULL
);


ALTER TABLE public.holding_stock OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16773)
-- Name: holding_stock_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.holding_stock_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.holding_stock_id_seq OWNER TO postgres;

--
-- TOC entry 4828 (class 0 OID 0)
-- Dependencies: 219
-- Name: holding_stock_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.holding_stock_id_seq OWNED BY public.holding_stock.id;


--
-- TOC entry 224 (class 1259 OID 16814)
-- Name: price_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.price_history (
    id bigint NOT NULL,
    currency_id bigint NOT NULL,
    price numeric(18,8) NOT NULL,
    "timestamp" timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.price_history OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16813)
-- Name: price_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.price_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.price_history_id_seq OWNER TO postgres;

--
-- TOC entry 4829 (class 0 OID 0)
-- Dependencies: 223
-- Name: price_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.price_history_id_seq OWNED BY public.price_history.id;


--
-- TOC entry 222 (class 1259 OID 16792)
-- Name: trades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trades (
    id bigint NOT NULL,
    wallet_id bigint NOT NULL,
    currency_id bigint NOT NULL,
    price numeric(18,8) NOT NULL,
    quantity numeric(18,8) NOT NULL,
    action character varying(10) NOT NULL,
    profit numeric(18,8) DEFAULT 0,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    "timestamp" timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT trades_action_check CHECK (((action)::text = ANY ((ARRAY['BUY'::character varying, 'SELL'::character varying])::text[]))),
    CONSTRAINT trades_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'SUCCESS'::character varying, 'FAILED'::character varying])::text[])))
);


ALTER TABLE public.trades OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16791)
-- Name: trades_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trades_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trades_id_seq OWNER TO postgres;

--
-- TOC entry 4830 (class 0 OID 0)
-- Dependencies: 221
-- Name: trades_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trades_id_seq OWNED BY public.trades.id;


--
-- TOC entry 218 (class 1259 OID 16766)
-- Name: wallet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wallet (
    id bigint NOT NULL,
    capital numeric(18,8) DEFAULT 0 NOT NULL
);


ALTER TABLE public.wallet OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16765)
-- Name: wallet_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.wallet_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.wallet_id_seq OWNER TO postgres;

--
-- TOC entry 4831 (class 0 OID 0)
-- Dependencies: 217
-- Name: wallet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.wallet_id_seq OWNED BY public.wallet.id;


--
-- TOC entry 4649 (class 2604 OID 16760)
-- Name: currency id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.currency ALTER COLUMN id SET DEFAULT nextval('public.currency_id_seq'::regclass);


--
-- TOC entry 4652 (class 2604 OID 16777)
-- Name: holding_stock id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.holding_stock ALTER COLUMN id SET DEFAULT nextval('public.holding_stock_id_seq'::regclass);


--
-- TOC entry 4658 (class 2604 OID 16817)
-- Name: price_history id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.price_history ALTER COLUMN id SET DEFAULT nextval('public.price_history_id_seq'::regclass);


--
-- TOC entry 4654 (class 2604 OID 16795)
-- Name: trades id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trades ALTER COLUMN id SET DEFAULT nextval('public.trades_id_seq'::regclass);


--
-- TOC entry 4650 (class 2604 OID 16769)
-- Name: wallet id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wallet ALTER COLUMN id SET DEFAULT nextval('public.wallet_id_seq'::regclass);


--
-- TOC entry 4663 (class 2606 OID 16764)
-- Name: currency currency_coingecko_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.currency
    ADD CONSTRAINT currency_coingecko_id_key UNIQUE (coingecko_id);


--
-- TOC entry 4665 (class 2606 OID 16762)
-- Name: currency currency_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.currency
    ADD CONSTRAINT currency_pkey PRIMARY KEY (id);


--
-- TOC entry 4669 (class 2606 OID 16780)
-- Name: holding_stock holding_stock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.holding_stock
    ADD CONSTRAINT holding_stock_pkey PRIMARY KEY (id);


--
-- TOC entry 4673 (class 2606 OID 16820)
-- Name: price_history price_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.price_history
    ADD CONSTRAINT price_history_pkey PRIMARY KEY (id);


--
-- TOC entry 4671 (class 2606 OID 16802)
-- Name: trades trades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trades_pkey PRIMARY KEY (id);


--
-- TOC entry 4667 (class 2606 OID 16772)
-- Name: wallet wallet_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wallet
    ADD CONSTRAINT wallet_pkey PRIMARY KEY (id);


--
-- TOC entry 4674 (class 2606 OID 16786)
-- Name: holding_stock holding_stock_currency_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.holding_stock
    ADD CONSTRAINT holding_stock_currency_id_fkey FOREIGN KEY (currency_id) REFERENCES public.currency(id) ON DELETE CASCADE;


--
-- TOC entry 4675 (class 2606 OID 16781)
-- Name: holding_stock holding_stock_wallet_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.holding_stock
    ADD CONSTRAINT holding_stock_wallet_id_fkey FOREIGN KEY (wallet_id) REFERENCES public.wallet(id) ON DELETE CASCADE;


--
-- TOC entry 4678 (class 2606 OID 16821)
-- Name: price_history price_history_currency_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.price_history
    ADD CONSTRAINT price_history_currency_id_fkey FOREIGN KEY (currency_id) REFERENCES public.currency(id) ON DELETE CASCADE;


--
-- TOC entry 4676 (class 2606 OID 16808)
-- Name: trades trades_currency_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trades_currency_id_fkey FOREIGN KEY (currency_id) REFERENCES public.currency(id) ON DELETE CASCADE;


--
-- TOC entry 4677 (class 2606 OID 16803)
-- Name: trades trades_wallet_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trades_wallet_id_fkey FOREIGN KEY (wallet_id) REFERENCES public.wallet(id) ON DELETE CASCADE;


-- Completed on 2025-09-04 16:33:40

--
-- PostgreSQL database dump complete
--



