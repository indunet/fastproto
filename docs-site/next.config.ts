import type { NextConfig } from "next";

const basePath = process.env.BASE_PATH?.trim() || "";

const nextConfig: NextConfig = {
  output: "export",
  basePath: basePath || undefined,
  images: { unoptimized: true },
  trailingSlash: false,
  /** Hide the floating “N” dev tools icon (Preferences / route info, etc.) in the corner during `next dev`. */
  devIndicators: false,
};

export default nextConfig;
