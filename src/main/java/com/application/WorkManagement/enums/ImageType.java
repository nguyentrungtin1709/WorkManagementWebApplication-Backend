package com.application.WorkManagement.enums;

public enum ImageType {
    JPEG {
        @Override
        public String toString(){
            return "image/jpeg";
        }
    },

    PNG {
        @Override
        public String toString(){
            return "image/png";
        }
    }
}
