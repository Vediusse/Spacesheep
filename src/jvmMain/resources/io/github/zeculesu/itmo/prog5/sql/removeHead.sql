DELETE
FROM collection
WHERE id = (SELECT id FROM collection LIMIT 1);