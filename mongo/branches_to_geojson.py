# -*- coding: utf-8 -*-
import argparse
import logging
import sys
import json

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
    parser = argparse.ArgumentParser(description="Branches GeoJSON converter")

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
        help="The input file from where branches data will be taken. Must contain a valid JSON.",
        action='store',
        type=str
    )

    parser.add_argument(
        '-o',
        '--output',
        dest="output_file",
        help="The output file where branches data in GeoJSON format will be stored.",
        action='store',
        type=str
    )

    return parser.parse_args(args)


def main(args):
    args = parse_args(args)
    setup_logging(args.log_level)
    _logger.info('JSON to GeoJSON')

    input_file = args.input_file
    output_file = args.output_file

    _logger.info('Reading input from {}.'.format(input_file))
    with open(input_file) as in_file:
        branches = json.load(in_file)
    _logger.info('Finished reading input.')

    _logger.info('Converting to GeoJSON.')
    branches_geojson = map(lambda branch: {
        'type': 'Feature',
        'properties': {
            'companyId': branch['companyId'],
            'address': branch['address'],
            'description': branch['description'],
            'type': branch['type'],
            'openTime': branch['openTime'],
            'province': branch['province'],
            'city': branch['city'],
            'score': branch['score'],
            'branchType': branch['branchType'],
            'isReportable': branch['isReportable'],
            'types': branch['types'],
        },
        'geometry': {
            'type': 'Point',
            'coordinates': [
                branch['lon'],
                branch['lat']
            ]
        }
    }, branches)
    _logger.info('Finished converting to GeoJSON.')

    _logger.info('Writing to {} output file'.format(output_file))
    with open(output_file, 'w') as out_file:
        json.dump(
            obj=branches_geojson,
            fp=out_file,
            indent=4,
            separators=(',', ': '),
            sort_keys=True)
    _logger.info('Finished writing output.')

    _logger.info("Finished")


def run():
    """Entry point for console_scripts
    """
    main(sys.argv[1:])


if __name__ == "__main__":
    run()
