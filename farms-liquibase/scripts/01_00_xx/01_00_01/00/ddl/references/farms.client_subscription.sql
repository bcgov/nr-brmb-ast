ALTER TABLE client_subscription ADD CONSTRAINT fk_cs_ac1 
    FOREIGN KEY (agristability_client_id)
    REFERENCES agristability_client(agristability_client_id)
;

ALTER TABLE client_subscription ADD CONSTRAINT fk_cs_ar 
    FOREIGN KEY (agristability_representative_id)
    REFERENCES agristability_representative(agristability_representative_id)
;

ALTER TABLE client_subscription ADD CONSTRAINT fk_cs_ssc 
    FOREIGN KEY (subscription_status_code)
    REFERENCES subscription_status_code(subscription_status_code)
;
