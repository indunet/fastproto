import * as React from "react";
import { Slot } from "@radix-ui/react-slot";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/cn";

const buttonVariants = cva(
  [
    "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-lg",
    "text-sm font-semibold",
    "transition-all duration-150",
    "focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-blue-500/40 focus-visible:ring-offset-2",
    "disabled:pointer-events-none disabled:opacity-40",
    "[&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0",
  ].join(" "),
  {
    variants: {
      variant: {
        /* Main CTA — solid professional blue */
        primary: [
          "bg-blue-600 text-white border border-blue-700/30",
          "shadow-sm",
          "hover:bg-blue-700",
          "active:bg-blue-800",
        ].join(" "),
        /* Secondary action — clean white */
        default: [
          "bg-white text-gray-700 border border-gray-300",
          "shadow-sm",
          "hover:bg-gray-50 hover:border-gray-400",
          "active:bg-gray-100",
        ].join(" "),
        /* Outlined — blue border */
        outline: [
          "bg-transparent text-blue-600 border border-blue-300",
          "hover:bg-blue-50 hover:border-blue-400",
          "active:bg-blue-100",
        ].join(" "),
        /* Minimal — no border, for nav/toolbar */
        ghost: [
          "bg-transparent text-gray-600 border border-transparent",
          "hover:bg-gray-100 hover:text-gray-900",
        ].join(" "),
      },
      size: {
        default: "h-9 px-4 py-2",
        sm: "h-8 px-3 text-xs",
        lg: "h-10 px-6 text-base",
        icon: "h-9 w-9 px-0",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  },
);

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : "button";
    return (
      <Comp
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    );
  },
);
Button.displayName = "Button";

export { Button, buttonVariants };
