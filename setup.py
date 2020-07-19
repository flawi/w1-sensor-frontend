import setuptools

with open("README.md", "r") as fh:
    long_description = fh.read()

with open("LICENSE.txt", "r") as fh:
    module_license = fh.read()

setuptools.setup(
    name="w1therm2influx",
    version="0.1.0",
    license=module_license,
    long_description=long_description,
    long_description_content_type="text/markdown",

    packages=setuptools.find_packages(where="src"),
    package_dir={"": "src"},
    include_package_data=True,
    package_data={
        # If any package contains *.txt or *.rst files, include them:
        "": ["*.txt", "*.md"],
    },
    python_requires=">=3.5.*",
    install_requires=[
        "w1thermsensor~=1.3.0",
        "influxdb~=5.3.0"
    ],
    entry_points={
        "console_scripts": [
            "w1therm2influx = w1therm2influx.cli:cli"
        ]
    },
    zip_safe=False
)
