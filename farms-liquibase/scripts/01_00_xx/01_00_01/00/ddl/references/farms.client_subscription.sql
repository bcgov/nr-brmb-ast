ALTER TABLE farms.farm_client_subscriptions ADD CONSTRAINT fk_cs_ac1 
    FOREIGN KEY (agristability_client_id)
    REFERENCES farms.farm_agristability_clients(agristability_client_id)
;

ALTER TABLE farms.farm_client_subscriptions ADD CONSTRAINT fk_cs_ar 
    FOREIGN KEY (agristability_represntve_id)
    REFERENCES farms.farm_agristability_represntves(agristability_represntve_id)
;

ALTER TABLE farms.farm_client_subscriptions ADD CONSTRAINT fk_cs_ssc 
    FOREIGN KEY (subscription_status_code)
    REFERENCES farms.farm_subscription_status_codes(subscription_status_code)
;
