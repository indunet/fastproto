import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/cn";

const cardVariants = cva(
  [
    "relative overflow-hidden rounded-[28px]",
    "border border-[rgba(89,73,53,0.08)]",
    "bg-[linear-gradient(180deg,rgba(255,255,255,0.72),rgba(255,251,246,0.94))]",
    "shadow-[0_24px_60px_rgba(58,41,23,0.08)] backdrop-blur-sm",
    "transition duration-300",
    "before:pointer-events-none before:absolute before:inset-x-0 before:top-0 before:h-px before:bg-[linear-gradient(90deg,transparent,rgba(255,255,255,0.75),transparent)]",
  ].join(" "),
  {
    variants: {
      variant: {
        default: "",
        elevated: "shadow-[0_30px_75px_rgba(58,41,23,0.12)]",
        accent: [
          "border-[rgba(110,141,137,0.16)]",
          "bg-[linear-gradient(180deg,rgba(219,232,229,0.7),rgba(255,251,246,0.96))]",
        ].join(" "),
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
