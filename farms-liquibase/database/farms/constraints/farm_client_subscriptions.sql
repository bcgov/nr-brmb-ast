ALTER TABLE farms.farm_client_subscriptions ADD CONSTRAINT farm_csub_farm_asc_fk FOREIGN KEY (agristability_client_id) REFERENCES farms.farm_agristability_clients(agristability_client_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_client_subscriptions ADD CONSTRAINT farm_csub_farm_asr_fk FOREIGN KEY (agristability_represntve_id) REFERENCES farms.farm_agristability_represntves(agristability_represntve_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_client_subscriptions ADD CONSTRAINT farm_csub_farm_fssc_fk FOREIGN KEY (subscription_status_code) REFERENCES farms.farm_subscription_status_codes(subscription_status_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
