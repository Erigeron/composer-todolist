(ns composed.templates)

(def templates {
 "container" {
  :id "system", :type :box, :layout nil
  :props {}
  :attrs {}
  :presets #{}
  :style {"border" "1px solid #ddd", "padding" "8px", "margin" "8px", "background-color" "white"}
  :children {
   "T" {
    :id "T", :type :box, :layout :row-parted
    :props {}
    :attrs {}
    :presets #{}
    :style {}
    :children {
     "T" {
      :id "T", :type :text, :layout nil
      :props {"value" "some header"}
      :attrs {}
      :presets #{:font-fancy}
      :style {}
      :children {}
     }
     "j" {
      :id "j", :type :icon, :layout nil
      :props {"name" "settings"}
      :attrs {}
      :presets #{}
      :style {}
      :children {}
     }
    }
   }
  }
 }
 "footer" {
  :id "system", :type :box, :layout :row
  :props {}
  :attrs {}
  :presets #{}
  :style {"background-color" "hsl(0,0%,100%)", "padding" "8px"}
  :children {
   "T" {
    :id "T", :type :button, :layout nil
    :props {"text" "Clear"}
    :attrs {}
    :presets #{}
    :style {}
    :children {}
   }
   "j" {
    :id "j", :type :space, :layout nil
    :props {"width" "8"}
    :attrs {}
    :presets #{}
    :style {}
    :children {}
   }
  }
 }
 "header" {
  :id "system", :type :box, :layout :row-middle
  :props {}
  :attrs {}
  :presets #{}
  :style {"background-color" "hsl(0,0%,100%)", "padding" "8px"}
  :children {
   "T" {
    :id "T", :type :text, :layout nil
    :props {"value" "Todolist demo"}
    :attrs {}
    :presets #{:font-fancy}
    :style {}
    :children {}
   }
   "b" {
    :id "b", :type :space, :layout nil
    :props {"width" "8"}
    :attrs {}
    :presets #{}
    :style {}
    :children {}
   }
   "j" {
    :id "j", :type :input, :layout nil
    :props {}
    :attrs {"placeholder" "Todo thing"}
    :presets #{}
    :style {}
    :children {}
   }
   "n" {
    :id "n", :type :space, :layout nil
    :props {"width" "8"}
    :attrs {}
    :presets #{}
    :style {}
    :children {}
   }
   "r" {
    :id "r", :type :button, :layout nil
    :props {}
    :attrs {}
    :presets #{}
    :style {}
    :children {}
   }
  }
 }
 "records" {
  :id "system", :type :list, :layout :row
  :props {}
  :attrs {}
  :presets #{}
  :style {}
  :children {
   "T" {
    :id "T", :type :box, :layout nil
    :props {}
    :attrs {}
    :presets #{}
    :style {}
    :children {
     "T" {
      :id "T", :type :box, :layout nil
      :props {}
      :attrs {}
      :presets #{}
      :style {}
      :children {}
     }
    }
   }
  }
 }
})
