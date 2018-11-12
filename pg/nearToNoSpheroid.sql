SELECT 
    id, 
    ST_AsText(location) as location,
    branch_type,
    score,
    description,
    is_reportable,
    company_id,
    city,
    address,
    province,
    open_time,
    type_value,
    types 
FROM sube_branches_geography
WHERE ST_DWithin(location, ('SRID=4326;POINT(' || :lon || ' ' || :lat || ')')::geography, :distance, false);
