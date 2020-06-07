import setuptools

with open("README.md", "r") as fh:
    long_description = fh.read()

with open("LICENSE.txt", "r") as fh:
    module_license = fh.read()

setuptools.setup(
    name="w1term2influx",
    version="0.1.0",
    packages=["w1term2influx"],
    license=module_license,
    long_description=long_description,
    long_description_content_type="text/markdown"
)
