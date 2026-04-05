export type DocNavItem = {
  slug: string;
  title: string;
};

/** Documentation sidebar: slug ↔ markdown file in ../docs */
export const DOC_NAV: DocNavItem[] = [
  { slug: "quick-start", title: "Quick Start" },
  { slug: "annotation-mapping", title: "Annotation Mapping" },
  { slug: "byte-and-bit-order", title: "Byte & Bit Order" },
  { slug: "checksum", title: "Checksum / CRC" },
  { slug: "expect", title: "Expect Assertions" },
  { slug: "formulas", title: "Transformation Formulas" },
  { slug: "arrays-and-strings", title: "Arrays & Strings" },
  { slug: "variable-length", title: "Variable Length & Struct Arrays" },
  { slug: "dynamic-offset", title: "Dynamic Offset (offsetRef)" },
  { slug: "without-annotations", title: "APIs without Annotations" },
  { slug: "android", title: "Android" },
  { slug: "netty-integration", title: "Netty Integration" },
  { slug: "kafka-integration", title: "Kafka Integration" },
  { slug: "faq", title: "FAQ" },
];

export const DOC_SLUGS = DOC_NAV.map((i) => i.slug);
