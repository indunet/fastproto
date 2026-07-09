export type DocNavItem = {
  slug: string;
  title: string;
  kicker: string;
  summary: string;
};

/** Documentation sidebar: slug ↔ markdown file in ../docs */
export const DOC_NAV: DocNavItem[] = [
  {
    slug: "quick-start",
    title: "Quick Start",
    kicker: "First Steps",
    summary: "Install the library, define a packet class, and complete your first encode/decode loop.",
  },
  {
    slug: "annotation-mapping",
    title: "Annotation Mapping",
    kicker: "Field Design",
    summary: "Understand how annotations map protocol bytes to Java fields with concise, readable models.",
  },
  {
    slug: "byte-and-bit-order",
    title: "Byte & Bit Order",
    kicker: "Low-Level Control",
    summary: "Configure endianness and bit ordering precisely for hardware-oriented or mixed-layout payloads.",
  },
  {
    slug: "checksum",
    title: "Checksum / CRC",
    kicker: "Integrity",
    summary: "Apply built-in CRC and checksum strategies without bolting on custom validation code.",
  },
  {
    slug: "expect",
    title: "Expect Assertions",
    kicker: "Validation",
    summary: "Protect parsing flows with declarative expectations and fail fast when payloads drift.",
  },
  {
    slug: "formulas",
    title: "Transformation Formulas",
    kicker: "Value Logic",
    summary: "Attach encode/decode transformations for calibration, scaling, or custom engineering rules.",
  },
  {
    slug: "arrays-and-strings",
    title: "Arrays & Strings",
    kicker: "Collections",
    summary: "Model repeated values and string segments cleanly while keeping layouts deterministic.",
  },
  {
    slug: "variable-length",
    title: "Variable Length & Struct Arrays",
    kicker: "Dynamic Layouts",
    summary: "Work with packets whose lengths and nested structures change with runtime data.",
  },
  {
    slug: "dynamic-offset",
    title: "Dynamic Offset (offsetRef)",
    kicker: "Addressing",
    summary: "Use referenced offsets to map fields that shift position according to previous bytes.",
  },
  {
    slug: "without-annotations",
    title: "APIs without Annotations",
    kicker: "Alternate API",
    summary: "Use the lower-level builder and utility APIs when annotation mapping is not the right fit.",
  },
  {
    slug: "android",
    title: "Android",
    kicker: "Platform",
    summary: "Integrate FastProto in Android projects with a lean setup for mobile runtime constraints.",
  },
  {
    slug: "netty-integration",
    title: "Netty Integration",
    kicker: "Integration",
    summary: "Plug FastProto into Netty pipelines for transport-friendly binary parsing and encoding.",
  },
  {
    slug: "kafka-integration",
    title: "Kafka Integration",
    kicker: "Streaming",
    summary: "Serialize binary payloads into Kafka producers and consumers without glue-heavy adapters.",
  },
  {
    slug: "ros2-messages",
    title: "ROS2 Messages",
    kicker: "Robotics Data",
    summary: "Decode and encode standard ROS 2 CDR messages in Java without a ROS 2 runtime.",
  },
  {
    slug: "ros2-bag",
    title: "ROS2 Bag Reading",
    kicker: "Robotics Data",
    summary: "Open sqlite3 or MCAP rosbag2 recordings and iterate messages with optional CDR decoding.",
  },
  {
    slug: "faq",
    title: "FAQ",
    kicker: "Reference",
    summary: "Find quick answers to common setup, behavior, and troubleshooting questions.",
  },
];

export const DOC_SLUGS = DOC_NAV.map((i) => i.slug);

export function getDocNavItem(slug: string) {
  return DOC_NAV.find((item) => item.slug === slug);
}

export function getDocNeighbors(slug: string) {
  const index = DOC_NAV.findIndex((item) => item.slug === slug);
  if (index === -1) {
    return { previous: null, next: null };
  }

  return {
    previous: index > 0 ? DOC_NAV[index - 1] : null,
    next: index < DOC_NAV.length - 1 ? DOC_NAV[index + 1] : null,
  };
}
