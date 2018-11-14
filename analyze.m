function [sums, means, stds, vars, medians, data] = analyze(data_dir, output_dir)
	pg_geometry = dlmread(strcat(data_dir, '/', 'pg-geometry.txt'));
	pg_geography_spheroid = dlmread(strcat(data_dir, '/', 'pg-geography-spheroid.txt'));
	pg_geography_nospheroid = dlmread(strcat(data_dir, '/', 'pg-geography-nospheroid.txt'));
	mongo_2D = dlmread(strcat(data_dir, '/', 'mongo-2D.txt'));
	mongo_2DSphere = dlmread(strcat(data_dir, '/', 'mongo-2DSphere.txt'));

	data = [pg_geometry, pg_geography_spheroid, pg_geography_nospheroid, mongo_2D, mongo_2DSphere];

	sums = sum(data);
	means = mean(data);
	stds = std(data);
	vars = var(data);
	medians = median(data);
