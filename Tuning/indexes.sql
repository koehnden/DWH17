-- primary key (evtl. unötig )
ALTER TABLE einwohner
ADD CONSTRAINT pk_einwohner PRIMARY KEY (id);

-- Bitmap indexe (waren für Query 1 und 2 besser als das B-Baum equivalent)
CREATE BITMAP INDEX bit_map_vater_mutter ON einwohner (vaterid, mutterid, 1);
CREATE BITMAP INDEX idx_wohnort ON einwohner (wohnort);

-- function based index (beide zu teuer und daher nutzlos)
CREATE INDEX idx_month ON einwohner (extract(month from geburtsdatum));
CREATE INDEX idx_hausnr ON einwohner (regexp_replace(adresse, '[^0-9]', ''))
