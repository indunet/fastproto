import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/cn";

const cardVariants = cva(
  [
    "relative rounded-xl bg-white",
    "border border-gray-200",
    /* Clean business shadow — no glow, just depth */
    "shadow-sm",
    "transition-shadow duration-200",
  ].join(" "),
  {
    variants: {
      variant: {
        default: "",
        elevated: "shadow-md",
        /* Optional blue-tinted card for hero/highlight use */
        accent: "border-blue-200 bg-blue-50/40",
      },
    },
    defaultVariants: { variant: "default" },
  },
);

export interface CardProps
  extends React.HTMLAttributes<HTMLDivElement>,
    VariantProps<typeof cardVariants> {}

export function Card({ className, variant, children, ...props }: CardProps) {
  return (
    <div className={cn(cardVariants({ variant }), className)} {...props}>
      {children}
    </div>
  );
}
