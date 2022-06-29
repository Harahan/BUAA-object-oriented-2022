public class AvlTree<T extends Comparable<T>> {
    private Node root = null;

    private int size(Node node) {
        return node == null ? 0 : node.size;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    public void add(T key) {
        root = (root == null) ? new Node(key) : root.add(key);
    }

    public void delete(T key) {
        root = root.delete(key);
    }

    public int getRank(T key) {
        return root.getRank(key, 1);
    }

    public class Node {
        private Node left = null;
        private Node right = null;
        private int height = 1;
        private int size = 1;
        private int cnt = 1;
        private T key;

        private Node(T key) {
            this.key = key;
        }

        private void pushUp() {
            height = Math.max(height(left), height(right)) + 1;
            size = size(left) + size(right) + cnt;
        }

        private Node leftRotate() {
            Node newRoot = right;
            right = newRoot.left;
            newRoot.left = this;
            pushUp();
            newRoot.pushUp();
            return newRoot;
        }

        private Node rightRotate() {
            Node newRoot = left;
            left = newRoot.right;
            newRoot.right = this;
            pushUp();
            newRoot.pushUp();
            return newRoot;
        }

        private int factor() {
            return height(left) - height(right);
        }

        private Node leftCheck() {
            int innerBf = right.factor();
            if (innerBf > 0) {
                right = right.rightRotate();
            }
            return leftRotate();
        }

        private Node rightCheck() {
            int innerBf = left.factor();
            if (innerBf < 0) {
                left = left.leftRotate();
            }
            return rightRotate();
        }

        private Node check() {
            int bf = factor();
            if (bf < -1) {
                return leftCheck();
            } else if (bf > 1) {
                return rightCheck();
            }
            pushUp();
            return this;
        }

        private void leftAdd(T key) {
            left = (left == null) ? new Node(key) : left.add(key);
        }

        private void rightAdd(T key) {
            right = (right == null) ? new Node(key) : right.add(key);
        }

        private Node add(T key) {
            if (key.compareTo(this.key) < 0) {
                leftAdd(key);
            } else if (key.compareTo(this.key) > 0) {
                rightAdd(key);
            } else {
                cnt += 1;
            }
            return check();
        }

        private Node deleteMin(Node node) {
            if (this == node) {
                return right;
            }
            left = left.deleteMin(node);
            return check();
        }

        private Node rightReplace() {
            Node newNode = leftRotate();
            newNode.left = null;
            return newNode.check();
        }

        private Node leftReplace() {
            Node newNode = rightRotate();
            newNode.right = null;
            return newNode.check();
        }

        private Node getRightMin() {
            Node iter = right;
            while (iter.left != null) {
                iter = iter.left;
            }
            return iter;
        }

        private Node selfDelete() {
            if (left == null && right == null) {
                return null;
            } else if (left == null || right == null) {
                return (left == null) ? rightReplace() : leftReplace();
            }
            Node iter = getRightMin();
            key = iter.key;
            cnt = iter.cnt;
            right = this.right.deleteMin(iter);
            return check();
        }

        private Node delete(T key) {
            if (key.compareTo(this.key) == 0) {
                cnt -= 1;
                size -= 1;
                return (cnt > 0) ? this : selfDelete();
            } else if (key.compareTo(this.key) < 0) {
                left = left.delete(key);
            } else {
                right = right.delete(key);
            }
            return check();
        }

        private int getRank(T key, int rank) {
            if (key.compareTo(this.key) < 0) {
                return left.getRank(key, rank);
            } else if (key.compareTo(this.key) > 0) {
                return right.getRank(key, rank + size(left) + cnt);
            }
            return rank + size(left);
        }

    }
}
