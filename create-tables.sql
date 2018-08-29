--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.10
-- Dumped by pg_dump version 10.5 (Ubuntu 10.5-0ubuntu0.18.04)

-- Started on 2018-08-28 13:14:42 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12393)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2135 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 186 (class 1259 OID 24588)
-- Name: sensor-values; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."sensor-values" (
    id integer NOT NULL,
    "sensor-id" character varying(15),
    "sensor-value" numeric(6,3),
    "timestamp" timestamp without time zone DEFAULT timezone('utc'::text, now())
);


ALTER TABLE public."sensor-values" OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 24586)
-- Name: sensor-values_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."sensor-values_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."sensor-values_id_seq" OWNER TO postgres;

--
-- TOC entry 2136 (class 0 OID 0)
-- Dependencies: 185
-- Name: sensor-values_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."sensor-values_id_seq" OWNED BY public."sensor-values".id;


--
-- TOC entry 2005 (class 2604 OID 24591)
-- Name: sensor-values id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."sensor-values" ALTER COLUMN id SET DEFAULT nextval('public."sensor-values_id_seq"'::regclass);


--
-- TOC entry 2009 (class 2606 OID 24594)
-- Name: sensor-values sensor-values_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."sensor-values"
    ADD CONSTRAINT "sensor-values_pkey" PRIMARY KEY (id);


--
-- TOC entry 2007 (class 1259 OID 32778)
-- Name: sensor-id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "sensor-id" ON public."sensor-values" USING btree ("sensor-id");


--
-- TOC entry 2010 (class 1259 OID 32779)
-- Name: timestamp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "timestamp" ON public."sensor-values" USING btree ("timestamp");


-- Completed on 2018-08-28 13:14:48 CEST

--
-- PostgreSQL database dump complete
--
