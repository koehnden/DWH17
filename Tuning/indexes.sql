-- primary key (evtl. unötig )
ALTER TABLE einwohner
ADD CONSTRAINT pk_einwohner PRIMARY KEY (id);

-- Bitmap indexe (waren für Query 1 und 2 besser als das B-Baum equivalent)
CREATE BITMAP INDEX bitmap_vater_mutter ON einwohner (vaterid, mutterid, 1);
-- CREATE BITMAP INDEX idx_bundesland ON einwohner (bundesland); -- mit Materialized View ersetzt

-- function based index (evtl zu teuer zu bauen)
CREATE INDEX idx_month ON einwohner (extract(month from geburtsdatum));

-- Materialized View für vaterid or mutterid not NULL
CREATE MATERIALIZED VIEW LOG ON einwohner
WITH SEQUENCE, ROWID (id)
INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW vater_mutter_not_null
BUILD IMMEDIATE
ENABLE QUERY REWRITE
AS
SELECT id, vaterid, mutterid, bundesland
FROM einwohner
WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL);

-- index auf Materialized View
CREATE INDEX idx_mv_bundesland ON vater_mutter_not_null (bundesland);
