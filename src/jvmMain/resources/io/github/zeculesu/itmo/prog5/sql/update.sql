UPDATE collection
SET name                = ?,
    coordinatesX        = ?,
    coordinatesY        = ?,
    creationDate        = ?,
    health              = ?,
    category            = CAST(? AS ASTARTESCATEGORY),
    weaponType          = CAST(? AS WEAPONTYPE),
    meleeWeapon         = CAST(? AS MELEEWEAPON),
    chapterName         = ?,
    chapterParentLegion = ?
WHERE id = ?;