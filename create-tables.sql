--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.10
-- Dumped by pg_dump version 10.5 (Ubuntu 10.5-0ubuntu0.18.04)

-- Started on 2018-09-01 10:50:30 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "w1-sensor-frontend-db";
--
-- TOC entry 2142 (class 1262 OID 16384)
-- Name: w1-sensor-frontend-db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "w1-sensor-frontend-db" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'de_DE.UTF-8' LC_CTYPE = 'de_DE.UTF-8';


ALTER DATABASE "w1-sensor-frontend-db" OWNER TO postgres;

\connect -reuse-previous=on "dbname='w1-sensor-frontend-db'"

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
-- TOC entry 2 (class 3079 OID 12393)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2145 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 1 (class 3079 OID 40971)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 2146 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 188 (class 1259 OID 40986)
-- Name: sensor-details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."sensor-details" (
    "sensor-id" character(12) NOT NULL,
    name character varying(150),
    description character varying(1500)
);


ALTER TABLE public."sensor-details" OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 24588)
-- Name: sensor-values; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."sensor-values" (
    id integer NOT NULL,
    "sensor-id" character (12),
    "sensor-value" numeric(6,3),
    "timestamp" timestamp without time zone DEFAULT timezone('utc'::text, now())
);


ALTER TABLE public."sensor-values" OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 24586)
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
-- TOC entry 2147 (class 0 OID 0)
-- Dependencies: 186
-- Name: sensor-values_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."sensor-values_id_seq" OWNED BY public."sensor-values".id;


--
-- TOC entry 2011 (class 2604 OID 24591)
-- Name: sensor-values id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."sensor-values" ALTER COLUMN id SET DEFAULT nextval('public."sensor-values_id_seq"'::regclass);


--
-- TOC entry 2018 (class 2606 OID 40990)
-- Name: sensor-details sensor-details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."sensor-details"
    ADD CONSTRAINT "sensor-details_pkey" PRIMARY KEY ("sensor-id");


--
-- TOC entry 2014 (class 2606 OID 24594)
-- Name: sensor-values sensor-values_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."sensor-values"
    ADD CONSTRAINT "sensor-values_pkey" PRIMARY KEY (id);


--
-- TOC entry 2019 (class 1259 OID 41001)
-- Name: sensor-details_sensor-id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "sensor-details_sensor-id_idx" ON public."sensor-details" USING btree ("sensor-id");


--
-- TOC entry 2015 (class 1259 OID 40994)
-- Name: sensor-values_sensor-id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "sensor-values_sensor-id_idx" ON public."sensor-values" USING btree ("sensor-id");


--
-- TOC entry 2016 (class 1259 OID 32779)
-- Name: sensor-values_timestamp_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "sensor-values_timestamp_idx" ON public."sensor-values" USING btree ("timestamp");


--
-- TOC entry 2144 (class 0 OID 0)
-- Dependencies: 8
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2018-09-01 10:50:36 CEST

--
-- PostgreSQL database dump complete
--
