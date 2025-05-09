ALTER TABLE farms.client_subscription ADD CONSTRAINT fk_cs_ac1 
    FOREIGN KEY (agristability_client_id)
    REFERENCES farms.agristability_client(agristability_client_id)
;

ALTER TABLE farms.client_subscription ADD CONSTRAINT fk_cs_ar 
    FOREIGN KEY (agristability_representative_id)
    REFERENCES farms.agristability_representative(agristability_representative_id)
;

ALTER TABLE farms.client_subscription ADD CONSTRAINT fk_cs_ssc 
    FOREIGN KEY (subscription_status_code)
    REFERENCES farms.subscription_status_code(subscription_status_code)
;
