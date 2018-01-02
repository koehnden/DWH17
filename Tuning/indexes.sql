
-- zusammengesetzer b-baum vater mutter index mit null werte 
CREATE INDEX idx_vater_mutter ON einwohner (vaterid, mutterid, 1);
DROP INDEX idx_vater_mutter


-- bitmap indexe
CREATE BITMAP INDEX bit_map_vater_mutter ON einwohner (vaterid, mutterid, 1);

-- function based index (ignored by oracle -> find workaround)
CREATE INDEX idx_hausnr ON einwohner (regexp_replace(adresse, '[^0-9]', ''))
DROP INDEX idx_hausnr
