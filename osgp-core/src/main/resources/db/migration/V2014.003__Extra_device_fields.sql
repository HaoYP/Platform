
ALTER TABLE public.device ADD COLUMN container_code character varying(255);
ALTER TABLE public.device ADD COLUMN container_city character varying(255);
ALTER TABLE public.device ADD COLUMN container_street character varying(255);
ALTER TABLE public.device ADD COLUMN gps_latitude character varying(15);
ALTER TABLE public.device ADD COLUMN gps_longitude character varying(15);
