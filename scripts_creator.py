# -*- coding: utf-8 -*-
import argparse
import json
import logging
import sys

_logger = logging.getLogger(__name__)


def setup_logging(log_level):
    """Setup basic logging
    Args:
      log_level (int): minimum log level for emitting messages
    """
    logging.basicConfig(level=log_level,
                        stream=sys.stdout,
                        format="[%(asctime)s] %(levelname)s:%(name)s: %(message)s",
                        datefmt="%Y-%m-%d %H:%M:%S")


def parse_args(args):
    """Parse command line parameters
    Args:
      args ([str]): command line parameters as list of strings
    Returns:
      :obj:`argparse.Namespace`: command line parameters namespace
    """
    parser = argparse.ArgumentParser(description="cities GeoJSON converter")

    # Logging
    parser.add_argument(
        '-v',
        '--verbose',
        dest="log_level",
        help="Set log level to INFO.",
        action='store_const',
        const=logging.INFO)
    parser.add_argument(
        '-vv',
        '--very-verbose',
        dest="log_level",
        help="Set log level to DEBUG.",
        action='store_const',
        const=logging.DEBUG)

    parser.add_argument(
        '-i',
        '--input',
        dest="input_file",
        help="The input file from where cities data will be taken. Must contain a valid GeoJson.",
        action='store',
        type=str
    )

    parser.add_argument(
        '-o',
        '--output',
        dest="output_dir",
        help="The output directory where scripts will be saved.",
        action='store',
        type=str
    )

    return parser.parse_args(args)


def main(args):
    args = parse_args(args)
    setup_logging(args.log_level)
    _logger.info('Creating testing scripts')

    input_file = args.input_file
    output_dir = args.output_dir

    _logger.info("Reading data file")
    with open(input_file) as in_file:
        cities = json.load(in_file)
    _logger.info("Finished reading data file")

    results = {
        # 'pg-geography-spheroid': [],
        # 'pg-geography-nospheroid': [],
        'pg-geometry': [],
        # 'mongo-2D': [],
        # 'mongo-2DSphere': [],
    }

    _logger.info("Creating scripts")
    for city in cities:
        x, y = city['geometry']['coordinates']

        # ========================================================================================================
        # PG Spheroid
        command = 'psql spatialdb-tpe -f pg/nearToTest.sql -v spheroid=true -v lon={} -v lat={} | grep Time' \
            .format(x, y)
        results['pg-geography-spheroid'].append(command)
        # ========================================================================================================

        # ========================================================================================================
        # PG No Spheroid
        command = 'psql spatialdb-tpe -f pg/nearToTest.sql -v spheroid=false -v lon={} -v lat={} | grep Time' \
            .format(x, y)
        results['pg-geography-nospheroid'].append(command)
        # ========================================================================================================

        # ========================================================================================================
        # PG Geography
        command = 'psql spatialdb-tpe -f pg/nearToTestGeometry.sql -v spheroid=false -v lon={} -v lat={} | grep Time' \
            .format(x, y)
        results['pg-geometry'].append(command)
        # ========================================================================================================

        # ========================================================================================================
        # Mongo 2D
        command = 'mongo spatialdb-tpe mongo/nearToTest2D.js --eval=\"var longitude={}; var latitude={}\" ' \
                  '| grep \"Time elapsed\"' \
            .format(x, y)
        results['mongo-2D'].append(command)
        # ========================================================================================================

        # ========================================================================================================
        # Mongo 2D Sphere
        command = 'mongo spatialdb-tpe mongo/nearToTest2DSphere.js --eval=\"var longitude={}; var latitude={}\" ' \
                  '| grep \"Time elapsed\"' \
            .format(x, y)
        results['mongo-2DSphere'].append(command)
        # ========================================================================================================

    _logger.info("Finished creating scripts")

    _logger.info("Saving script files")
    for (key, commands_list) in results.items():
        with open('{}/{}.sh'.format(output_dir, key), 'w') as f:
            for command in commands_list:
                f.write("%s\n" % command)
    _logger.info("Finished saving script files")

    _logger.info("Finished")


def run():
    """Entry point for console_scripts
    """
    main(sys.argv[1:])


if __name__ == "__main__":
    run()
